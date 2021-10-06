package app.chat.controller;

import app.chat.entity.channel.Channel;
import app.chat.model.ApiResponse;
import app.chat.model.dto.ChannelDto;
import app.chat.model.dto.ChannelMemberDto;
import app.chat.model.req.AttachRoleDto;
import app.chat.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getChannel(@PathVariable(value = "id") Long id) {
        return channelService.getChannel(id);
    }

    @GetMapping("/myChannel")
    public ResponseEntity<?> getMyChannels(@RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber) {
        return channelService.getMyChannels(pageNumber);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<?> getMembers(@PathVariable(value = "id") Long channelId,
                                        @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber) {
        return channelService.getMembers(channelId, pageNumber);
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody ChannelDto dto) {
        return channelService.createChannel(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable(value = "id") Long id, @RequestBody ChannelDto dto) {
        return channelService.updateChannel(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        return channelService.deleteChannel(id);
    }

    //    Channel members
    @GetMapping("/get/member/sort/{channelId}")
    public ResponseEntity<?> getMemberSort(@PathVariable(value = "channelId") Long id,
                                           @RequestParam(value = "sortBy", defaultValue = "date") String sortBy,
                                           @RequestParam(value = "sortType", defaultValue = "asc") String sortType,
                                           @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber) {
        return channelService.getMemberSort(id, sortBy, sortType, pageNumber);
    }

    @GetMapping("/get/blocklist/{id}")
    public ResponseEntity<?> getBlockList(@PathVariable Long id,
                                          @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber) {
        return channelService.getBlockList(id, pageNumber);
    }


    @PostMapping("/add/member")
    public ResponseEntity<?> addMember(@Valid @RequestBody ChannelMemberDto dto) {
        return channelService.addMember(dto);
    }

    @PostMapping("/add/member/{link}")
    public ResponseEntity<?> addMemberByLink(@PathVariable String link) {
        return channelService.addMemberByLink(link);
    }

    @PostMapping("/attach/role")
    public ResponseEntity<?> attachRole(@Valid @RequestBody AttachRoleDto dto) {
        return channelService.attachRole(dto);
    }

    @PostMapping("/give/admin/{id}")
    public ResponseEntity<?> giveAdmin(@PathVariable(value = "id") Long channelId,
                                       @RequestParam(value = "channelUserId") Long channelUserId) {
        return channelService.giveAdmin(channelId, channelUserId);
    }

    @PostMapping("/block/member/{id}")
    public ResponseEntity<?> blockMember(@PathVariable Long id, @RequestParam(value = "userId") Long userId) {
        return channelService.blockMember(id, userId);
    }

    @PostMapping("/unblock/member/{id}")
    public ResponseEntity<?> unblockMember(@PathVariable Long id, @RequestParam(value = "userId") Long userId) {
        return channelService.unblockMember(id, userId);
    }

    @DeleteMapping("/remove/member/{id}")
    public ResponseEntity<?> removeMember(@PathVariable Long id, @RequestParam(value = "userId") Long userId) {
        return channelService.removeMember(id, userId);
    }

}
