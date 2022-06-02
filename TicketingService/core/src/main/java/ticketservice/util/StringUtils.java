package ticketservice.util;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class StringUtils {

    public static Long toLongId(String id) {
        return Long.valueOf(id.substring(1));
    }

}
