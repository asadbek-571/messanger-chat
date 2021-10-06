package app.chat.common;

import app.chat.entity.ChatRole;
import app.chat.entity.Error;
import app.chat.entity.settings.Language;
import app.chat.enums.ChatPermissions;
import app.chat.enums.ChatRoleEnum;
import app.chat.enums.ErrorSeries;
import app.chat.enums.Lang;
import app.chat.repository.ChatRoleRepo;
import app.chat.repository.ErrorRepo;
import app.chat.repository.sittings.LanguageRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class DataLoader implements CommandLineRunner {
    private final LanguageRepo languageRepo;
    private final ErrorRepo errorRepo;
    private final ChatRoleRepo chatRoleRepo;

    public DataLoader(LanguageRepo languageRepo, ErrorRepo errorRepo, ChatRoleRepo chatRoleRepo) {
        this.languageRepo = languageRepo;
        this.errorRepo = errorRepo;
        this.chatRoleRepo = chatRoleRepo;
    }

    @Value("${spring.datasource.initialization-mode}")
    private String initialMode;

    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")) {
            List<Language> languages = new ArrayList<>();
            languages.add(new Language("O`zbekcha", Lang.UZ));
            languages.add(new Language("Русский", Lang.RU));
            languages.add(new Language("English", Lang.EN));

            languageRepo.saveAll(languages);

            List<Error> errors = new ArrayList<>();
            errors.add(new Error("Bu identifikatorda foydalanuvchi topilmadi : %s", 10, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("Пользователь не найден с этим идентификатором : %s", 10, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("No user found in this id : %s", 10, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errors.add(new Error("Bu identifikatorda kanal topilmadi : %s", 20, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("Канал с этим идентификатором не найден : %s", 20, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("No channel found in this id : %s", 20, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errors.add(new Error("Bu identifikatorda gurux topilmadi : %s", 30, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("Группа с этим идентификатором не найдена : %s", 30, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("No group found in this id : %s", 30, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errors.add(new Error("Bu identifikatorda chat topilmadi : %s", 40, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("С этим идентификатором чата не найдено : %s", 40, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("No chat found in this id : %s", 40, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errors.add(new Error("Bu identifikatorda chat topilmadi : %s", 50, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("Имя пользователя с этим идентификатором не найдено : %s", 50, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("No username found in this id : %s", 50, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errors.add(new Error("Bu identifikatorda sozlamalar topilmadi : %s", 60, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("В этом идентификаторе настроек не найдено : %s", 60, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("No settings found in this id : %s", 60, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errors.add(new Error("Bu identifikatorda shaxsiy chat topilmadi : %s", 70, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("В этом идентификаторе нет личного чата : %s", 70, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("No personal chat found in this id : %s", 70, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errors.add(new Error("Bu id da hech qanday xabar topilmadi : %s", 80, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("По этому идентификатору сообщений не найдено : %s", 80, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("No message found in this id : %s", 80, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errors.add(new Error("Bu id da hech qanday fayl topilmadi : %s", 90, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("Файл с этим идентификатором не найден : %s", 90, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("No file found in this id : %s", 90, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errors.add(new Error("Bu foydalanuvchi nomi allaqachon mavjud : %s", 51, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("Такое имя пользователя уже существует : %s", 51, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("This username already exists : %s", 51, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errors.add(new Error("Tavsif juda katta maksimal hajmi : %s", 41, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("Слишком большое описание макс размер: %s", 41, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("Description too large max size : %s", 41, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errors.add(new Error("Bu id da hech qanday bildirishnoma topilmadi : %s", 61, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("По этому идентификатору уведомление не найдено : %s", 61, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("No notification found in this id : %s", 61, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errors.add(new Error("Bu id da hech qanday bildirishnoma topilmadi : %s", 62, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("По этому идентификатору уведомление не найдено : %s", 62, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("No notification found in this id : %s", 62, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errors.add(new Error("Bu id da hech qanday bildirishnoma topilmadi : %s", 63, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("По этому идентификатору уведомление не найдено : %s", 63, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("No notification found in this id : %s", 63, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errors.add(new Error("Bu foydalanuvchi bilan chat yaratib bo`lmadi : %s", 71, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("Не удалось создать чат с этим пользователем : %s", 71, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("Could not create chat with this user : %s", 71, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errors.add(new Error("Ushbu elektron pochta ro`yxatdan o`tib bo`lgan : %s", 11, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("Этот адрес уже зарегистрирован : %s", 11, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("This email address is already registered : %s", 11, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errors.add(new Error("Elektron pochta manzili noto‘g‘ri : %s", 12, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("Адрес электронной почты неверный : %s", 12, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("The email address is incorrect : %s", 12, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errors.add(new Error("Ushbu id dagi foydalanuvchini bloklagansiz : %s", 14, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("Вы заблокировали пользователя с этим идентификатором : %s", 14, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("You have blocked this id user : %s", 14, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errors.add(new Error("Ushbu id dagi foydalanuvchi sizni bloklagan : %s", 15, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("Пользователь с этим идентификатором заблокировал вас : %s", 15, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("The user on this id has blocked you : %s", 15, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errors.add(new Error("Ushbu id li xabarni ko`rilgan deb belgilay olmaysiz : %s", 81, languageRepo.getByCode(Lang.UZ), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("Вы не можете пометить сообщение с этим идентификатором как просмотренное : %s", 81, languageRepo.getByCode(Lang.RU), ErrorSeries.CLIENT_ERROR));
            errors.add(new Error("You cannot mark a message with this id as viewed : %s", 81, languageRepo.getByCode(Lang.EN), ErrorSeries.CLIENT_ERROR));

            errorRepo.saveAll(errors);

            List<ChatRole> roles = new ArrayList<>();
            roles.add(new ChatRole(ChatRoleEnum.OWNER, ChatPermissions.getOwnerPermission()));
            roles.add(new ChatRole(ChatRoleEnum.ADMIN, ChatPermissions.getAdminPermission()));
            roles.add(new ChatRole(ChatRoleEnum.MEMBER, ChatPermissions.getMemberPermission()));

            chatRoleRepo.saveAll(roles);
        }
    }
}
