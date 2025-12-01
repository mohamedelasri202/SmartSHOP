package com.example.smartshop.ServiceTest;

import com.example.smartshop.DTO.ClientCreationDto;
import com.example.smartshop.DTO.ClientDto;
import com.example.smartshop.Exceptions.BusinessValidationException;
import com.example.smartshop.Mapper.ClientMapper;
import com.example.smartshop.Models.Client;
import com.example.smartshop.Models.User;
import com.example.smartshop.Models.Enums.LoyaltyLevel;
import com.example.smartshop.Repositories.ClientRepository;
import com.example.smartshop.Repositories.UserRepository;
import com.example.smartshop.Services.ClientServiceImpl;
import com.example.smartshop.Util.PasswordEncoder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    // --- Dependencies to be Mocked ---
    @Mock
    private UserRepository userRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientMapper clientMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    // --- Service under Test (Injects the Mocks) ---
    @InjectMocks
    private ClientServiceImpl clientService;

    // --- Test Data ---
    private ClientCreationDto creationDto;
    private User mockUser;
    private ClientDto expectedClientDto;
    // Note: mockClient field removed from class level as we use a local Spy.

    @BeforeEach
    void setUp() {
        // Setup the input DTO
        creationDto = new ClientCreationDto();
        creationDto.setUsername("testuser");
        creationDto.setPassword("rawpassword");
        creationDto.setName("Test Corp");
        creationDto.setEmail("test@corp.com");

        // Setup mock entities that will be returned after saving
        mockUser = new User();
        mockUser.setId(1L); // Simulate generated User ID

        // Setup the expected output DTO
        expectedClientDto = new ClientDto();
        expectedClientDto.setId(5L); // Simulate generated Client ID
    }


    @Test
    void createClient_Success() {

        // ARRANGE (Setup the Client Entity as a SPY)

        // Create a real Client entity object
        Client realClient = new Client();

        // CRITICAL FIX: Turn the real object into a SPY so we can verify setter calls
        Client clientSpy = spy(realClient);

        // 1. Validation: Username is NOT present
        when(userRepository.findByUsername(creationDto.getUsername())).thenReturn(Optional.empty());

        // 2. Security: Mock password hashing
        when(passwordEncoder.encode(creationDto.getPassword())).thenReturn("hashed_password_123");

        // 3. Mapper: When DTO -> Entity conversion runs, return the SPY object
        when(clientMapper.toClientEntity(creationDto)).thenReturn(clientSpy);

        // 4. JPA: Mock saving the User and Client entities
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // When the service saves the Client, we return the spy (simulating ID generation)
        when(clientRepository.save(any(Client.class))).thenReturn(clientSpy);

        // 5. Mapper: Mock Entity -> DTO conversion (returns the final output)
        when(clientMapper.toDto(clientSpy)).thenReturn(expectedClientDto);


        // ACT (Execute the method under test)
        ClientDto result = clientService.createClient(creationDto);


        // ASSERT (Verify the results and internal interactions on the SPY)

        assertNotNull(result);
        assertEquals(5L, result.getId(), "The returned DTO should have the generated Client ID.");

        // Verification on the SPY (CRITICAL FIX)
        // 1. Verify that the User object was correctly linked to the Client entity
        verify(clientSpy, times(1)).setUser(mockUser);

        // 2. Verify that the required default fields were set on the client entity
        verify(clientSpy, times(1)).setLoyaltyLevel(LoyaltyLevel.BASIC);
        verify(clientSpy, times(1)).setTotalSpent(BigDecimal.ZERO);

        // Verify critical data save calls were executed
        verify(userRepository, times(1)).save(any(User.class));
        verify(clientRepository, times(1)).save(clientSpy); // Client save must use the SPY/real object
    }


    // ----------------------------------------------------------------------------------
    // SCENARIO 2: Validation Failure (Username Exists) - No changes needed here.
    // ----------------------------------------------------------------------------------
    @Test
    void createClient_UsernameExists_ThrowsException() {
        // ARRANGE
        // Mock the User being found (already exists)
        when(userRepository.findByUsername(creationDto.getUsername())).thenReturn(Optional.of(mockUser));

        // ASSERT & ACT
        assertThrows(BusinessValidationException.class,
                () -> clientService.createClient(creationDto),
                "Should throw BusinessValidationException when username already exists.");

        // Verify that no save operations were attempted after validation failed
        verify(userRepository, never()).save(any(User.class));
        verify(clientRepository, never()).save(any(Client.class));
    }
}