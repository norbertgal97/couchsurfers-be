package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.mapper.CouchMapper;
import com.norbertgal.couchsurfersbe.api.v1.mapper.CouchPhotoMapper;
import com.norbertgal.couchsurfersbe.api.v1.mapper.CouchPreviewMapper;
import com.norbertgal.couchsurfersbe.api.v1.model.*;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.*;
import com.norbertgal.couchsurfersbe.domain.Couch;
import com.norbertgal.couchsurfersbe.domain.CouchPhoto;
import com.norbertgal.couchsurfersbe.domain.Location;
import com.norbertgal.couchsurfersbe.domain.User;
import com.norbertgal.couchsurfersbe.repositories.CouchPhotoRepository;
import com.norbertgal.couchsurfersbe.repositories.CouchRepository;
import com.norbertgal.couchsurfersbe.repositories.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.*;

@Profile("dev")
@Service
public class MyCouchServiceImpl implements MyCouchService {

    private final CouchRepository couchRepository;
    private final UserRepository userRepository;
    private final CouchPhotoRepository couchPhotoRepository;

    private final CouchMapper couchMapper;
    private final CouchPreviewMapper couchPreviewMapper;
    private final CouchPhotoMapper couchPhotoMapper;

    public MyCouchServiceImpl(CouchRepository couchRepository,
                              UserRepository userRepository,
                              CouchPhotoRepository couchPhotoRepository,
                              CouchMapper couchMapper,
                              CouchPreviewMapper couchPreviewMapper,
                              CouchPhotoMapper couchPhotoMapper) {
        this.couchRepository = couchRepository;
        this.userRepository = userRepository;
        this.couchPhotoRepository = couchPhotoRepository;
        this.couchMapper = couchMapper;
        this.couchPreviewMapper = couchPreviewMapper;
        this.couchPhotoMapper = couchPhotoMapper;
    }

    @Override
    public CouchDTO createCouch(CouchDTO couchDTO, long userId) throws NotFoundException, EmptyFieldsException, EntityAlreadyExistsException, UnknownUserException {
        if (!couchIsValid(couchDTO)) {
            throw new EmptyFieldsException(StatusDTO.builder()
                    .timestamp(new Date())
                    .errorCode(422)
                    .errorMessage("Empty fields!")
                    .build());
        }

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty())
            throw new UnknownUserException(StatusDTO.builder().timestamp(new Date()).errorCode(500).errorMessage("User is not found!").build());

        Optional<Couch> optionalCouch = couchRepository.findById(userId); // host_id

        if (optionalCouch.isPresent())
            throw new EntityAlreadyExistsException(StatusDTO.builder().timestamp(new Date()).errorCode(409).errorMessage("A different object with the same identifier value was already associated with the session!").build());

        User user = optionalUser.get();

        Couch couch = couchMapper.couchDTOtoCouch(couchDTO);
        couch.setHosted(false);
        couch.setUser(user);

        Couch savedCouch = couchRepository.save(couch);

        return couchMapper.couchToCouchDTO(savedCouch);
    }

    @Override
    public CouchDTO updateCouch(Map<String, Object> fields, long userId, Long couchId) throws NotFoundException, EmptyFieldsException, WrongIdentifierException {
        if (!couchMapIsValid(fields)) {
            throw new EmptyFieldsException(StatusDTO.builder().timestamp(new Date()).errorCode(422).errorMessage("Empty fields!").build());
        }

        if (userId != couchId) {
            throw new WrongIdentifierException(StatusDTO.builder().timestamp(new Date()).errorCode(403).errorMessage("You can't access this resource!").build());
        }

        Optional<Couch> optionalCouch = couchRepository.findById(couchId);

        if (optionalCouch.isEmpty()) {
            throw new NotFoundException(StatusDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Couch is not found!").build());
        }

        Couch couch = optionalCouch.get();
        Location couchLocation = couch.getLocation();

        if (fields.get(CouchDTO.CodingKeys.location.toString()) != null) {
            Map<?, ?> location = (Map<?, ?>) fields.get(CouchDTO.CodingKeys.location.toString());

            patchLocation(location, couchLocation);

            fields.remove(CouchDTO.CodingKeys.location.toString());
        }

        patchCouch(fields, couch);

        Couch savedCouch = couchRepository.save(couch);
        return couchMapper.couchToCouchDTO(savedCouch);
    }

    @Override
    public CouchDTO getCouch(Long couchId, Long userId) throws NotFoundException {
        Optional<Couch> optionalCouch = couchRepository.findById(couchId);

        if (optionalCouch.isEmpty()) {
            throw new NotFoundException(StatusDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Couch is not found!").build());
        }

        return couchMapper.couchToCouchDTO(optionalCouch.get());
    }

    public CouchPreviewDTO getNewestCouch() throws NotFoundException {
        Optional<Couch> optionalCouch = couchRepository.findFirstByOrderByIdDesc();

        if (optionalCouch.isEmpty()) {
            throw new NotFoundException(StatusDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Newest couch is not found!").build());
        }

        return couchPreviewMapper.couchToCouchPreviewDTO(optionalCouch.get());
    }

    @Override
    public List<CouchPhotoDTO> uploadImages(Long couchId, MultipartFile[] images, Long userId) throws WrongIdentifierException, NotFoundException, EmptyFileException, IOException {
        if (!couchId.equals(userId)) {
            throw new WrongIdentifierException(StatusDTO.builder().timestamp(new Date()).errorCode(403).errorMessage("You can't access this resource!").build());
        }

        Optional<Couch> optionalCouch = couchRepository.findById(couchId);

        if (optionalCouch.isEmpty()) {
            throw new NotFoundException(StatusDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Couch is not found!").build());
        }

        List<CouchPhoto> couchPhotos = new ArrayList<>();

        for(MultipartFile image: images) {
            CouchPhoto couchPhoto = new CouchPhoto();

            if(image.isEmpty()) {
                throw new EmptyFileException(StatusDTO.builder().timestamp(new Date()).errorCode(422).errorMessage("No file has been chosen or the chosen file has no content.").build());
            }

            try {
                couchPhoto.setPhoto(image.getBytes());
                couchPhoto.setCouch(optionalCouch.get());
                couchPhoto.setFileName(image.getOriginalFilename());
                couchPhoto.setType(image.getContentType());
                couchPhotos.add(couchPhoto);
            } catch (java.io.IOException ex) {
                throw new IOException(
                        StatusDTO.builder()
                        .timestamp(new Date())
                        .errorCode(422)
                        .errorMessage("Cannot read content of multipart files").build());
            }
        }

        List<CouchPhoto> savedImages = couchPhotoRepository.saveAll(couchPhotos);

        return couchPhotoMapper.toCouchPhotoDTOList(savedImages);
    }

    public byte[] downloadImage(Long couchId, Long imageId, Long userId) throws NotFoundException {
        Optional<CouchPhoto> optionalCouchPhoto = couchPhotoRepository.findById(imageId);

        if (optionalCouchPhoto.isEmpty()) {
            throw new NotFoundException(StatusDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Image is not found!").build());
        }

        CouchPhoto couchPhoto = optionalCouchPhoto.get();

        return couchPhoto.getPhoto();
    }

    @Override
    public MessageDTO deleteImages(Long couchId, FileDeleteDTO request, Long userId) throws  WrongIdentifierException {
        if (!couchId.equals(userId)) {
            throw new WrongIdentifierException(StatusDTO.builder().timestamp(new Date()).errorCode(403).errorMessage("You can't access this resource!").build());
        }

        for (Long id : request.getIds()) {
            couchPhotoRepository.deleteById(id);
        }

        return new MessageDTO("Images are successfully deleted!");
    }


    private void patchCouch(Map<String, Object> fields, Couch object) {
        fields.forEach((key, value) -> {
            try {
                CouchDTO.CodingKeys decodedKey = CouchDTO.CodingKeys.fromJsonProperty(key);

                updateField(Couch.class, decodedKey.toString(), object ,value);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }
        });
    }

    private void patchLocation(Map<?, ?> fields, Location object) {
        fields.forEach((key, value) -> {
            try {
                LocationDTO.CodingKeys decodedKey = LocationDTO.CodingKeys.fromJsonProperty((String) key);
                updateField(Location.class, decodedKey.toString(), object, value);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }
        });
    }

    private void updateField(Class<?> type, String decodedKey, Object object, Object value) {
        Field field = ReflectionUtils.findField(type, decodedKey);
        if (field != null) {
            field.setAccessible(true);
            ReflectionUtils.setField(field, object, value);
        }
    }

    private boolean couchIsValid(CouchDTO couchDTO) {
        String name = couchDTO.getName();
        Integer numberOfRooms = couchDTO.getNumberOfRooms();
        Integer numberOfGuests = couchDTO.getNumberOfGuests();
        Double price = couchDTO.getPrice();

        return name != null && !name.isEmpty() && locationIsValid(couchDTO.getLocation()) && numberOfRooms != null && numberOfGuests != null && price != null;
    }

    private boolean locationIsValid(LocationDTO locationDTO) {
        return locationDTO != null && locationDTO.getCity() != null && !locationDTO.getCity().isEmpty();
    }

    private boolean locationMapIsValid(Map<?, ?> location) {
        final String CITY = LocationDTO.CodingKeys.city.getJsonProperty();

        return !location.containsKey(CITY) || location.get(CITY) != null;
    }

    private boolean couchMapIsValid(Map<String, Object> couch) {
        final String NAME = CouchDTO.CodingKeys.name.getJsonProperty();
        final String NUMBER_OF_GUESTS = CouchDTO.CodingKeys.numberOfGuests.getJsonProperty();
        final String NUMBER_OF_ROOMS = CouchDTO.CodingKeys.numberOfRooms.getJsonProperty();
        final String PRICE = CouchDTO.CodingKeys.price.getJsonProperty();

        List<String> notNullFields = List.of(NAME, NUMBER_OF_GUESTS, NUMBER_OF_ROOMS, PRICE);

        for (String element : notNullFields) {
            if (couch.containsKey(element) && couch.get(element) == null) {
                return false;
            }
        }

        final String LOCATION = CouchDTO.CodingKeys.location.getJsonProperty();

        if (couch.containsKey(LOCATION) && couch.get(LOCATION) != null) {
            Map<?, ?> location = (Map<?, ?>) couch.get(LOCATION);
            return locationMapIsValid(location);

        } else return !couch.containsKey(LOCATION) || couch.get(LOCATION) != null;
    }
}
