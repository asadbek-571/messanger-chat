package app.chat.service;

import app.chat.enums.Lang;

public interface ErrorService {


    String message(Integer errorCode, Lang languageCode, Long id);

    String message(Integer errorCode, Lang languageCode, String name);

    String message(Integer errorCode, Lang languageCode);
}
