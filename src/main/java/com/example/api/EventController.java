package com.example.api;

import com.example.business.abstracts.IEventService;
import com.example.dao.CategoryRepo;
import com.example.dao.UserRepo;
import com.example.dto.request.event.EventSaveRequest;
import com.example.dto.request.event.EventUpdateRequest;
import com.example.dto.response.CursorResponse;
import com.example.dto.response.EventResponse;
import com.example.entities.Category;
import com.example.entities.Event;
import com.example.entities.User;
import com.example.mapper.IModelMapperService;
import com.example.result.ResultData;
import com.example.result.ResultHelper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoint'ler:
 * - POST    /v1/events        → Etkinlik oluşturma
 * - GET     /v1/events/{id}   → ID ile etkinlik getirme
 * - PUT     /v1/events        → Etkinlik güncelleme
 * - GET     /v1/events        → Sayfalı etkinlik listeleme
 * - DELETE  /v1/events/{id}   → Etkinlik silme
 */
@RestController
@RequestMapping("/v1/events")
public class EventController {
    private final IEventService eventService;
    private final IModelMapperService modelMapperService;
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;

    public EventController(IEventService eventService, IModelMapperService modelMapperService, CategoryRepo categoryRepo, UserRepo userRepo) {
        this.eventService = eventService;
        this.modelMapperService = modelMapperService;
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<EventResponse> save(@Valid @RequestBody EventSaveRequest request){
        Event eventToSave = this.modelMapperService.forRequest().map(request,Event.class);

        Category category = this.categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new com.example.exception.NotFoundException("Kategori bulunamadı. ID: " + request.getCategoryId()));

        User user = this.userRepo.findById(request.getUserId())
                .orElseThrow(() -> new com.example.exception.NotFoundException("Kullanıcı bulunamadı. ID: " + request.getUserId()));

        eventToSave.setCategory(category);
        eventToSave.setUser(user);

        Event savedEvent = this.eventService.save(eventToSave);
        EventResponse response = this.modelMapperService.forEventResponse().map(savedEvent,EventResponse.class);

        return ResultHelper.created(response);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<EventResponse> get(@PathVariable("id") Long id){
        Event event = this.eventService.get(id);
        EventResponse response = this.modelMapperService.forEventResponse().map(event,EventResponse.class);
        return ResultHelper.success(response);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<EventResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize
    ){
        Page<Event> eventPage = this.eventService.cursor(page,pageSize);
        Page<EventResponse> eventResponsePage = eventPage.map(event ->
                this.modelMapperService.forEventResponse().map(event,EventResponse.class)
        );

        return ResultHelper.cursor(eventResponsePage);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<EventResponse> update(@Valid @RequestBody EventUpdateRequest request){
        Event eventToUpdate = this.modelMapperService.forRequest().map(request,Event.class);

        Category category = this.categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new com.example.exception.NotFoundException("Kategori bulunamadı. ID: " + request.getCategoryId()));

        User user = this.userRepo.findById(request.getUserId())
                .orElseThrow(() -> new com.example.exception.NotFoundException("Kullanıcı bulunamadı. ID: " + request.getUserId()));

        eventToUpdate.setCategory(category);
        eventToUpdate.setUser(user);

        Event updatedEvent = this.eventService.update(eventToUpdate);
        EventResponse response = this.modelMapperService.forEventResponse().map(updatedEvent,EventResponse.class);

        return ResultHelper.success(response);
    }

}
