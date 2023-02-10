package shelter.backend.registration.service;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import shelter.backend.rest.feign.openapikrs.OpenApiKrsFeignClient;
import shelter.backend.rest.feign.openapikrs.req.Rejestr;
import shelter.backend.rest.feign.openapikrs.res.OpenApiKrsResponse;
import shelter.backend.rest.model.entity.Address;
import shelter.backend.rest.model.entity.User;
import shelter.backend.utils.converter.CharsNormalizer;


@Service
@Slf4j
public class OpenApiKrs implements ApprovalProvider {

    private final OpenApiKrsFeignClient openApiKrsFeignClient;

    public OpenApiKrs(OpenApiKrsFeignClient openApiKrsFeignClient) {
        this.openApiKrsFeignClient = openApiKrsFeignClient;
    }

    @Override
    public boolean validateShelterDetails(Address address) {
        String krs = address.getKRS_number();
        if (krs == null) {
            return false;
        }

        OpenApiKrsResponse response;

        try {
            //TODO: If there will be more Rejestrs consider @Rertry, @Recover provided by Spring
            response = openApiKrsFeignClient.getActualWriteOff(krs, Rejestr.P);
        } catch (FeignException.FeignClientException.NotFound e) {
            log.info("Shelter details not found for KRS: {}, Rejestr: {}", krs, Rejestr.P);
            log.info("Trying for Rejestr: {}", Rejestr.S);
            response = tryForRejestrS(krs);
        }
        if (response == null) {
            return false;
        }

        return checkShelterDetails(address, response);
    }

    private OpenApiKrsResponse tryForRejestrS(String krs) {
        try {
            return openApiKrsFeignClient.getActualWriteOff(krs, Rejestr.S);
        } catch (FeignException.FeignClientException.NotFound e) {
            log.info("Shelter details for KRS: {} not found in OpenApiKrs provider", krs);
            return null;
        }
    }

    private boolean checkShelterDetails(Address address, OpenApiKrsResponse response) {

        String cityRegistered = CharsNormalizer.convertToEngChars(address.getCity().trim());
        String cityResponse = CharsNormalizer.convertToEngChars(response.getOdpis().getDane().getDzial1().getSiedzibaIAdres().getAdres().getMiejscowosc().trim());
        String postalRegistered = address.getPostal_code().replace("-", "").trim();
        String postalResponse = response.getOdpis().getDane().getDzial1().getSiedzibaIAdres().getAdres().getKodPocztowy().replace("-", "").trim();
        String streetRegistered = CharsNormalizer.convertToEngChars(address.getStreet().trim());
        String streetResponse = CharsNormalizer.convertToEngChars(response.getOdpis().getDane().getDzial1().getSiedzibaIAdres().getAdres().getUlica().trim());
        String buildingRegistered = address.getBuilding_number().trim();
        String buildingResponse = response.getOdpis().getDane().getDzial1().getSiedzibaIAdres().getAdres().getNrDomu().trim();
        if (StringUtils.equalsIgnoreCase(cityRegistered, cityResponse)) {
            if (StringUtils.equals(postalRegistered, postalResponse)) {
                if (StringUtils.equalsIgnoreCase(streetRegistered, streetResponse)) {
                    if (StringUtils.equalsIgnoreCase(buildingRegistered, buildingResponse)) {
                        return true;
                    }
                }
            }
        }
        log.info("Shelter details are not valid. AddressRegistered: {},{},{},{} ResponseAddress: {},{},{},{}",
                cityRegistered, postalRegistered, streetRegistered, buildingRegistered,
                cityResponse, postalResponse, streetResponse, buildingResponse);
        return false;

    }
}
