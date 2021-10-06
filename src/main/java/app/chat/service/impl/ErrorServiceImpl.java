package app.chat.service.impl;

import app.chat.entity.Error;
import app.chat.enums.Lang;
import app.chat.helpers.Utils;
import app.chat.mapper.MapstructMapper;
import app.chat.repository.ErrorRepo;
import app.chat.service.ErrorService;
import app.chat.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ErrorServiceImpl implements ErrorService {

    private final ErrorRepo errorRepo;
    private final MapstructMapper mapstructMapper;
    private final LanguageService languageService;


    @Override
    public String message(Integer errorCode, Lang languageCode, Long id) {
        if (Utils.isEmpty(languageCode)) return null;

        if (!languageService.checkLanguageCode(languageCode)) {
            return null;
        }

        Optional<Error> errorOptional = errorRepo.findByCodeAndActiveAndLanguage_Code(
                errorCode,
                true,
                languageCode);


        if (errorOptional.isEmpty()) {
            return null;
        }

        Error error = errorOptional.get();

        error.setMessage(String.format(error.getMessage(), id));

        return mapstructMapper.toErrorDto(error).getMessage();
    }

    @Override
    public String message(Integer errorCode, Lang languageCode, String name) {
        if (Utils.isEmpty(languageCode)) return null;

        if (!languageService.checkLanguageCode(languageCode)) {
            return null;
        }

        Optional<Error> errorOptional = errorRepo.findByCodeAndActiveAndLanguage_Code(
                errorCode,
                true,
                languageCode);


        if (errorOptional.isEmpty()) {
            return null;
        }

        Error error = errorOptional.get();

        error.setMessage(String.format(error.getMessage(), name));

        return mapstructMapper.toErrorDto(error).getMessage();
    }

    public String message(Integer errorCode, Lang languageCode) {
        if (Utils.isEmpty(languageCode)) return null;

        if (!languageService.checkLanguageCode(languageCode)) {
            return null;
        }

        Optional<Error> errorOptional = errorRepo.findByCodeAndActiveAndLanguage_Code(
                errorCode,
                true,
                languageCode);


        if (errorOptional.isEmpty()) {
            return null;
        }

        Error error = errorOptional.get();

        return mapstructMapper.toErrorDto(error).getMessage();
    }


}
