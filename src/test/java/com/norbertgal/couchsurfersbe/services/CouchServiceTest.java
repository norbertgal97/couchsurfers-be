package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.BaseTest;
import com.norbertgal.couchsurfersbe.api.v1.model.CouchDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.LocationDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.EmptyFieldsException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.EntityAlreadyExistsException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.UnknownUserException;
import com.norbertgal.couchsurfersbe.domain.Couch;
import com.norbertgal.couchsurfersbe.domain.User;
import com.norbertgal.couchsurfersbe.repositories.CouchRepository;
import com.norbertgal.couchsurfersbe.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class CouchServiceTest extends BaseTest {

    @Autowired
    private CouchService couchService;

    @MockBean
    private  CouchRepository couchRepository;

    @MockBean
    private  UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = setUpUser();
        Couch couch = setUpCouch();

        couch.setUser(user);
        user.setCouch(couch);

        doReturn(Optional.of(user)).when(userRepository).findById(1L);
        doReturn(couch).when(couchRepository).save(any());
    }

    @Test
    @DisplayName("Create a couch without exceptions")
    void createCouch() {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCity("Budapest");

        CouchDTO couchDTO = new CouchDTO();
        couchDTO.setName("XY Apt");
        couchDTO.setNumberOfGuests(2);
        couchDTO.setNumberOfRooms(2);
        couchDTO.setPrice(2.0);
        couchDTO.setLocation(locationDTO);

        try {
            CouchDTO couch = couchService.createCouch(couchDTO, 1L);
            Assertions.assertEquals(2, couch.getNumberOfGuests());
            Assertions.assertEquals(2, couch.getNumberOfRooms());
            Assertions.assertEquals("Budapest", couch.getLocation().getCity());
            Assertions.assertEquals("XY Apt", couch.getName());

        } catch (EmptyFieldsException | EntityAlreadyExistsException | UnknownUserException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Create a couch with wrong user id")
    void createCouchWithWrongUserId() {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCity("Budapest");

        CouchDTO couchDTO = new CouchDTO();
        couchDTO.setName("XY Apartment");
        couchDTO.setNumberOfGuests(2);
        couchDTO.setNumberOfRooms(2);
        couchDTO.setPrice(2.0);
        couchDTO.setLocation(locationDTO);

        Exception exception = assertThrows(UnknownUserException.class, () -> {
            couchService.createCouch(couchDTO, 2L);
        });

        String expectedMessage = "User is not found!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Create a couch with invalid request")
    void createCouchWithInvalidRequest() {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCity("Budapest");

        CouchDTO couchDTO = new CouchDTO();
        couchDTO.setName("XY Apartment");
        couchDTO.setNumberOfGuests(2);
        couchDTO.setNumberOfRooms(2);
        couchDTO.setLocation(locationDTO);

        Exception exception = assertThrows(EmptyFieldsException.class, () -> {
            couchService.createCouch(couchDTO, 1L);
        });

        String expectedMessage = "Empty fields!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Create a couch with existing id")
    void createCouchWithExistingId() {
        Couch couch = setUpCouch();
        doReturn(Optional.of(couch)).when(couchRepository).findById(1L);

        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCity("Budapest");

        CouchDTO couchDTO = new CouchDTO();
        couchDTO.setName("XY Apartment");
        couchDTO.setNumberOfGuests(2);
        couchDTO.setNumberOfRooms(2);
        couchDTO.setPrice(2.0);
        couchDTO.setLocation(locationDTO);

        Exception exception = assertThrows(EntityAlreadyExistsException.class, () -> {
            couchService.createCouch(couchDTO, 1L);
        });

        String expectedMessage = "A different object with the same identifier value was already associated with the session!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Get couch with valid id")
    void getCouch() {
        Couch couch = setUpCouch();
        doReturn(Optional.of(couch)).when(couchRepository).findById(1L);

        try {
            couchService.getCouch(1L);
            Assertions.assertEquals(2, couch.getNumberOfGuests());
            Assertions.assertEquals(2, couch.getNumberOfRooms());
            Assertions.assertEquals("Budapest", couch.getLocation().getCity());
            Assertions.assertEquals("XY Apt", couch.getName());
        } catch (NotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Get couch with invalid id")
    void getCouchWithInvalidId() {
        Couch couch = setUpCouch();
        doReturn(Optional.of(couch)).when(couchRepository).findById(1L);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            couchService.getCouch(2L);
        });

        String expectedMessage = "Couch is not found!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
