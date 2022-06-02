package ticketservice.exception;

import hu.matray.partner.endpoint.exception.PartnerError;
import hu.matray.partner.endpoint.exception.PartnerErrorCode;
import hu.matray.partner.endpoint.exception.PartnerException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static hu.matray.partner.endpoint.exception.PartnerErrorCode.STATIC_JSON_READ_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionMapperTest {

    private static final int STATUS_CODE = 400;
    private final ExceptionMapper exceptionMapper = new ExceptionMapper();

    @ParameterizedTest
    @EnumSource(PartnerErrorCode.class)
    void map(PartnerErrorCode partnerErrorCode) {
        //GIVEN
        PartnerException sourceException = new PartnerException(STATUS_CODE, new PartnerError(partnerErrorCode));

        //WHEN
        CoreException coreException = exceptionMapper.map(sourceException);

        //THEN
        assertEquals(STATUS_CODE, coreException.getStatusCode());
        if (partnerErrorCode == STATIC_JSON_READ_ERROR) {
            assertEquals(CoreErrorCode.PARTNER_SERVICE_ERROR.getErrorCode(), coreException.getCoreError().getErrorCode());
        } else {
            assertEquals(CoreErrorCode.fromErrorCode(coreException.getCoreError().getErrorCode()).name(), partnerErrorCode.name());
        }
    }
}