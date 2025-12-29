package com.example.api;

import com.example.business.abstracts.ITicketTypeService;
import com.example.dao.EventRepo;
import com.example.dto.request.ticketType.TicketTypeSaveRequest;
import com.example.dto.response.TicketTypeResponse;
import com.example.entities.Event;
import com.example.entities.TicketType;
import com.example.exception.NotFoundException;
import com.example.mapper.IModelMapperService;
import com.example.result.ResultData;
import com.example.result.ResultHelper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/ticket-types")
public class TicketTypeController {
    private final ITicketTypeService ticketTypeService;
    private final IModelMapperService modelMapperService;
    private final EventRepo eventRepo;

    public TicketTypeController(ITicketTypeService ticketTypeService, IModelMapperService modelMapperService, EventRepo eventRepo) {
        this.ticketTypeService = ticketTypeService;
        this.modelMapperService = modelMapperService;
        this.eventRepo = eventRepo;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<TicketTypeResponse> save(@Valid @RequestBody TicketTypeSaveRequest request){
        TicketType ticketTypeToSave = this.modelMapperService.forRequest().map(request, TicketType.class);

        Event event = this.eventRepo.findById(request.getEventId())
                .orElseThrow(() -> new NotFoundException("Etkinlik bulunamadı. ID: " + request.getEventId()));

        ticketTypeToSave.setEvent(event);

        TicketType savedTicketType = this.ticketTypeService.save(ticketTypeToSave);
        TicketTypeResponse response = this.modelMapperService.forTicketTypeResponse().map(savedTicketType, TicketTypeResponse.class);
        return ResultHelper.created(response);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<TicketTypeResponse> get(@PathVariable("id") Long id) {
        TicketType ticketType = this.ticketTypeService.get(id);
        TicketTypeResponse response = this.modelMapperService.forTicketTypeResponse().map(ticketType, TicketTypeResponse.class);
        return ResultHelper.success(response);
    }
}
