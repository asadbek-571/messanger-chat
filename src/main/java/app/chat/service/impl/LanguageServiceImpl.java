package app.chat.service.impl;

import app.chat.enums.Lang;
import app.chat.repository.sittings.LanguageRepo;
import app.chat.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepo languageRepo;

    @Override
    public Boolean checkLanguageCode(Lang languageCode) {
        return languageRepo.findByCode(languageCode).isPresent();
    }
}
