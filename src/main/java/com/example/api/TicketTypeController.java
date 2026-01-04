package com.example.api;

import com.example.business.abstracts.ITicketTypeService;
import com.example.dao.EventRepo;
import com.example.dto.request.ticketType.TicketTypeSaveRequest;
import com.example.dto.request.ticketType.TicketTypeUpdateRequest;
import com.example.dto.response.CursorResponse;
import com.example.dto.response.TicketTypeResponse;
import com.example.entities.Event;
import com.example.entities.TicketType;
import com.example.exception.NotFoundException;
import com.example.mapper.IModelMapperService;
import com.example.result.Result;
import com.example.result.ResultData;
import com.example.result.ResultHelper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Endpoint'ler:
 * - POST    /v1/ticket-types           → Bilet tipi oluşturma
 * - GET     /v1/ticket-types/{id}      → ID ile bilet tipi getirme
 * - PUT     /v1/ticket-types           → Bilet tipi güncelleme
 * - GET     /v1/ticket-types           → Sayfalı bilet tipi listeleme
 * - DELETE  /v1/ticket-types/{id}      → Bilet tipi silme
 * - GET     /v1/ticket-types/event/{eventId} → Etkinliğe ait tüm bilet tiplerini listeleme
 */
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

        ticketTypeToSave.setId(null); // Yeni kayıt için id null olmalı

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

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<TicketTypeResponse> update(@Valid @RequestBody TicketTypeUpdateRequest request) {
        TicketType ticketTypeToUpdate = this.modelMapperService.forRequest().map(request, TicketType.class);

        Event event = this.eventRepo.findById(request.getEventId())
                .orElseThrow(() -> new NotFoundException("Etkinlik bulunamadı. ID: " + request.getEventId()));

        ticketTypeToUpdate.setEvent(event);

        TicketType updatedTicketType = this.ticketTypeService.update(ticketTypeToUpdate);
        TicketTypeResponse response = this.modelMapperService.forTicketTypeResponse().map(updatedTicketType, TicketTypeResponse.class);

        return ResultHelper.success(response);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<TicketTypeResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize
    ) {
        Page<TicketType> ticketTypePage = this.ticketTypeService.cursor(page, pageSize);
        Page<TicketTypeResponse> ticketTypeResponsePage = ticketTypePage.map(ticketType ->
                this.modelMapperService.forTicketTypeResponse().map(ticketType, TicketTypeResponse.class)
        );

        return ResultHelper.cursor(ticketTypeResponsePage);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        this.ticketTypeService.delete(id);
        return ResultHelper.ok();
    }

    @GetMapping("/event/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<TicketTypeResponse>> getByEvent(@PathVariable("eventId") Long eventId) {
        List<TicketType> ticketTypes = this.ticketTypeService.getByEventId(eventId);

        List<TicketTypeResponse> ticketTypeResponses = ticketTypes.stream()
                .map(ticketType -> this.modelMapperService.forTicketTypeResponse().map(ticketType, TicketTypeResponse.class))
                .collect(Collectors.toList());

        return ResultHelper.success(ticketTypeResponses);
    }
}
