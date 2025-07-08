package com.example.dinewise.service;

import com.example.dinewise.model.ApplicationStatus;
import com.example.dinewise.model.ManagerApplication;
import com.example.dinewise.model.ManagerStatus;
import com.example.dinewise.model.MessManager;
import com.example.dinewise.repo.ManagerApplicationRepo;
import com.example.dinewise.repo.MessManagerRepo;
import com.example.dinewise.repo.StudentRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MessManagerServiceTest {

    private MessManagerService messManagerService;

    private ManagerApplicationRepo applicationRepo;
    private MessManagerRepo messManagerRepo;
    private StudentRepo studentRepo;

    @BeforeEach
    public void setUp() throws Exception {
        // Create mocks
        applicationRepo = mock(ManagerApplicationRepo.class);
        messManagerRepo = mock(MessManagerRepo.class);
        studentRepo = mock(StudentRepo.class);

        // Create instance of service
        messManagerService = new MessManagerService();

        // Inject mocks via reflection
        injectField(messManagerService, "applicationRepo", applicationRepo);
        injectField(messManagerService, "messManagerRepo", messManagerRepo);
        injectField(messManagerService, "studentRepo", studentRepo);
    }

    private void injectField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    public void testApplyForManager_NewApplication_ReturnsTrue() {
        LocalDate appliedMonth = LocalDate.of(2025, 8, 1);
        when(applicationRepo.existsByStdIdAndAppliedMonth("2005001", appliedMonth)).thenReturn(false);

        boolean result = messManagerService.applyForManager("2005001", appliedMonth);

        assertTrue(result);
        verify(applicationRepo, times(1)).save(any(ManagerApplication.class));
    }

    @Test
    public void testApplyForManager_AlreadyExists_ReturnsFalse() {
        LocalDate appliedMonth = LocalDate.of(2025, 8, 1);
        when(applicationRepo.existsByStdIdAndAppliedMonth("2005001", appliedMonth)).thenReturn(true);

        boolean result = messManagerService.applyForManager("2005001", appliedMonth);

        assertFalse(result);
        verify(applicationRepo, never()).save(any());
    }

    @Test
    public void testGetActiveManager_Found() {
        MessManager manager = new MessManager();
        manager.setStdId("2005001");
        manager.setStatus(ManagerStatus.running);

        when(messManagerRepo.findByStdIdAndStatus("2005001", ManagerStatus.running))
                .thenReturn(Optional.of(manager));

        MessManager result = messManagerService.getActiveManager("2005001");

        assertNotNull(result);
        assertEquals("2005001", result.getStdId());
    }

    @Test
    public void testGetActiveManager_NotFound() {
        when(messManagerRepo.findByStdIdAndStatus("2005001", ManagerStatus.running))
                .thenReturn(Optional.empty());

        MessManager result = messManagerService.getActiveManager("2005001");

        assertNull(result);
    }

    @Test
    public void testGetByStdId_Found() {
        MessManager manager = new MessManager();
        manager.setStdId("2005001");

        when(messManagerRepo.findByStdId("2005001")).thenReturn(Optional.of(manager));

        MessManager result = messManagerService.getByStdId("2005001");

        assertNotNull(result);
        assertEquals("2005001", result.getStdId());
    }

    @Test
    public void testGetByStdId_NotFound() {
        when(messManagerRepo.findByStdId("2005001")).thenReturn(Optional.empty());

        MessManager result = messManagerService.getByStdId("2005001");

        assertNull(result);
    }
}
