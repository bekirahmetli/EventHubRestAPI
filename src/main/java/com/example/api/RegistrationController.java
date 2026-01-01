package com.example.api;

import com.example.business.abstracts.IRegistrationService;
import com.example.dao.TicketTypeRepo;
import com.example.dao.UserRepo;
import com.example.dto.request.registration.RegistrationSaveRequest;
import com.example.dto.request.registration.RegistrationUpdateRequest;
import com.example.dto.response.CursorResponse;
import com.example.dto.response.RegistrationResponse;
import com.example.entities.Registration;
import com.example.entities.TicketType;
import com.example.entities.User;
import com.example.mapper.IModelMapperService;
import com.example.result.ResultData;
import com.example.result.ResultHelper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/registrations")
public class RegistrationController {

    private final IRegistrationService registrationService;
    private final IModelMapperService modelMapperService;
    private final UserRepo userRepo;
    private final TicketTypeRepo ticketTypeRepo;

    public RegistrationController(IRegistrationService registrationService,
                                  IModelMapperService modelMapperService,
                                  UserRepo userRepo,
                                  TicketTypeRepo ticketTypeRepo) {
        this.registrationService = registrationService;
        this.modelMapperService = modelMapperService;
        this.userRepo = userRepo;
        this.ticketTypeRepo = ticketTypeRepo;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<RegistrationResponse> save(@Valid @RequestBody RegistrationSaveRequest request) {
        Registration registrationToSave = this.modelMapperService.forRequest().map(request, Registration.class);

        User user = this.userRepo.findById(request.getUserId())
                .orElseThrow(() -> new com.example.exception.NotFoundException("Kullanıcı bulunamadı. ID: " + request.getUserId()));

        TicketType ticketType = this.ticketTypeRepo.findById(request.getTicketTypeId())
                .orElseThrow(() -> new com.example.exception.NotFoundException("Bilet türü bulunamadı. ID: " + request.getTicketTypeId()));

        registrationToSave.setUser(user);
        registrationToSave.setTicketType(ticketType);

        Registration savedRegistration = this.registrationService.save(registrationToSave);
        RegistrationResponse response = this.modelMapperService.forRegistrationResponse().map(savedRegistration, RegistrationResponse.class);

        return ResultHelper.created(response);
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<RegistrationResponse> get(@PathVariable("id") Long id) {
        Registration registration = this.registrationService.get(id);
        RegistrationResponse response = this.modelMapperService.forRegistrationResponse().map(registration, RegistrationResponse.class);
        return ResultHelper.success(response);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<RegistrationResponse> update(@Valid @RequestBody RegistrationUpdateRequest request) {
        Registration registrationToUpdate = this.modelMapperService.forRequest().map(request, Registration.class);

        User user = this.userRepo.findById(request.getUserId())
                .orElseThrow(() -> new com.example.exception.NotFoundException("Kullanıcı bulunamadı. ID: " + request.getUserId()));

        TicketType ticketType = this.ticketTypeRepo.findById(request.getTicketTypeId())
                .orElseThrow(() -> new com.example.exception.NotFoundException("Bilet türü bulunamadı. ID: " + request.getTicketTypeId()));

        registrationToUpdate.setUser(user);
        registrationToUpdate.setTicketType(ticketType);

        Registration updatedRegistration = this.registrationService.update(registrationToUpdate);
        RegistrationResponse response = this.modelMapperService.forRegistrationResponse().map(updatedRegistration, RegistrationResponse.class);

        return ResultHelper.success(response);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<RegistrationResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize
    ) {
        Page<Registration> registrationPage = this.registrationService.cursor(page, pageSize);
        Page<RegistrationResponse> registrationResponsePage = registrationPage.map(registration ->
                this.modelMapperService.forRegistrationResponse().map(registration, RegistrationResponse.class)
        );

        return ResultHelper.cursor(registrationResponsePage);
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<RegistrationResponse>> getByUser(@PathVariable("userId") Long userId) {
        List<Registration> registrations = this.registrationService.getByUser(userId);

        List<RegistrationResponse> registrationResponses = registrations.stream()
                .map(registration -> this.modelMapperService.forRegistrationResponse().map(registration, RegistrationResponse.class))
                .collect(Collectors.toList());

        return ResultHelper.success(registrationResponses);
    }
}
