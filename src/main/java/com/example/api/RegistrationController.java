package com.example.api;

import com.example.business.abstracts.IRegistrationService;
import com.example.dao.TicketTypeRepo;
import com.example.dao.UserRepo;
import com.example.dto.request.registration.RegistrationSaveRequest;
import com.example.dto.response.RegistrationResponse;
import com.example.entities.Registration;
import com.example.entities.TicketType;
import com.example.entities.User;
import com.example.mapper.IModelMapperService;
import com.example.result.ResultData;
import com.example.result.ResultHelper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
