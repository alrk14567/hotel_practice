package com.example.hotel_booking.service.Impl;

import com.example.hotel_booking.dto.FacilityDto;
import com.example.hotel_booking.dto.HotelDto;
import com.example.hotel_booking.dto.HotelFileDto;
import com.example.hotel_booking.entity.*;
import com.example.hotel_booking.repository.*;
import com.example.hotel_booking.service.FileService;
import com.example.hotel_booking.service.HotelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final FacilityRepository facilityRepository;
    private final CityRepository cityRepository;
    private final HotelFacilityRepository hotelFacilityRepository;
    private final UserRepository userRepository;
    private final FileService fileService;




    public List<HotelEntity> searchHotel(List<Long> gradeList, List<Long> cityIdList
            , List<Long> facilityIdList, String hotelName) {

        List<HotelEntity> hotelDtoList = new ArrayList<>();

        List<Long> hotelIdListFindByGrade = new ArrayList<>();
        if (!gradeList.isEmpty()) {
            for (Long grade : gradeList) {
                hotelIdListFindByGrade.addAll(hotelRepository.findByGrade(grade));
            }
        } else {
            hotelIdListFindByGrade = hotelRepository.findAllId();
        }

        if (hotelIdListFindByGrade.isEmpty()) {
            return null;
        }
        Collections.sort(hotelIdListFindByGrade);

        List<Long> hotelIdListFindByCity = new ArrayList<>();
        if (!cityIdList.isEmpty()) {
            for (Long cityId : cityIdList) {
                hotelIdListFindByCity.addAll(hotelRepository.findByCityId(cityId));
            }
        } else {
            hotelIdListFindByCity = hotelRepository.findAllId();
        }

        if (hotelIdListFindByCity.isEmpty()) {
            return null;
        }
        Collections.sort(hotelIdListFindByCity);

        List<Long> hotelIdListFindByFacility = new ArrayList<>();
        if (!facilityIdList.isEmpty()) {
            for (Long facilityId : facilityIdList) {
                hotelIdListFindByFacility.addAll(facilityRepository.findAllByFacilityId(facilityId));
            }

            //key: hotel id, value: 나온 갯수
            Map<Long, Integer> hotelIdCount = new HashMap<>();
            for (Long hotelId : hotelIdListFindByFacility) {
                if (hotelIdCount.containsKey(hotelId)) {
                    hotelIdCount.put(hotelId, hotelIdCount.get(hotelId) + 1);
                } else {
                    hotelIdCount.put(hotelId, 1);
                }
            }

            for (Map.Entry<Long, Integer> entry : hotelIdCount.entrySet()) {
                if (entry.getValue() >= facilityIdList.size()) {
                    hotelIdListFindByFacility.add(entry.getKey());
                }
            }
        } else {
            hotelIdListFindByFacility = hotelRepository.findAllId();
        }

        if (hotelIdListFindByFacility.isEmpty()) {
            return null;
        }
        Collections.sort(hotelIdListFindByFacility);

        //Set<Long> roomTypeIdSet = new HashSet<>();

        List<Long> hotelIdListFindByHotelName;
        if (hotelName != null) {
            hotelIdListFindByHotelName = new ArrayList<>(hotelRepository.findByHotelNameContaining(hotelName));
        } else {
            hotelIdListFindByHotelName = hotelRepository.findAllId();
        }

        if (hotelIdListFindByHotelName.isEmpty()) {
            return null;
        }
        Collections.sort(hotelIdListFindByHotelName);

        int gradeIndex = 0, cityIndex = 0, facilityIndex = 0, hotelIndex = 0;

        while (gradeIndex < hotelIdListFindByGrade.size() &&
                cityIndex < hotelIdListFindByCity.size() &&
                facilityIndex < hotelIdListFindByFacility.size() &&
                hotelIndex < hotelIdListFindByHotelName.size()) {

            long gradeCurVal = hotelIdListFindByGrade.get(gradeIndex);
            long cityCurVal = hotelIdListFindByCity.get(cityIndex);
            long facilityCurVal = hotelIdListFindByFacility.get(facilityIndex);
            long hotelCurVal = hotelIdListFindByHotelName.get(hotelIndex);

            if (gradeCurVal == cityCurVal &&
                    cityCurVal == facilityCurVal &&
                    facilityCurVal == hotelCurVal) {
                HotelEntity tempHotelDto = hotelRepository.findById(gradeCurVal).get();

                hotelDtoList.add(tempHotelDto);
                gradeIndex++;
                cityIndex++;
                facilityIndex++;
                hotelIndex++;
            } else {
                long tempMin1 = Math.min(gradeCurVal, cityCurVal);
                long tempMin2 = Math.min(facilityCurVal, hotelCurVal);
                long minVal = Math.min(tempMin1, tempMin2);

                if (gradeCurVal == minVal)
                    gradeIndex++;
                if (cityCurVal == minVal)
                    cityIndex++;
                if (facilityCurVal == minVal)
                    facilityIndex++;
                if (hotelCurVal == minVal)
                    hotelIndex++;
            }
        }

        System.out.println("gradeList: " + hotelIdListFindByGrade);
        System.out.println("cityList: " + hotelIdListFindByCity);
        System.out.println("facilityList: " + hotelIdListFindByFacility);
        System.out.println("nameList: " + hotelIdListFindByHotelName);
        System.out.println(facilityIdList);
//        for (HotelDto hotelDto : hotelDtoList) {
//            System.out.println(hotelDto.toString());
//        }
        return hotelDtoList;
    }

    public Long save(HotelDto hotelDto) {

        HotelDto hotelDto = new HotelDto();
        hotelDto.setHotelName((String) valueMap.get("hotelName"));
        hotelDto.setHotelEmail((String) valueMap.get("hotelEmail"));
        hotelDto.setHotelPhone((String) valueMap.get("hotelPhone"));
        hotelDto.setHotelAddress((String) valueMap.get("hotelAddress"));
        System.out.println(valueMap.get("hotelGrade").toString());
        int hotelGrade = Integer.parseInt(valueMap.get("hotelGrade").toString());
        int cityId = Integer.parseInt(valueMap.get("cityId").toString());
        int userId = Integer.parseInt(valueMap.get("userId").toString());


        hotelDto.setHotelGrade((long) hotelGrade);
        hotelDto.setCityId((long) cityId);
        hotelDto.setUserId((long) userId);


        Long id = hotelService.save(hotelDto);


        List<FacilityDto> facilityDtoList = new ArrayList<>();
        System.out.println(valueMap.get("facilities").getClass());

        List<Integer> facilityList = (ArrayList<Integer>) valueMap.get("facilities");
        for (int i = 0; i < facilityList.size(); i++) {
            FacilityDto temp = new FacilityDto();
            temp.setHotelId(id);
            temp.setFacilityId(facilityList.get(i).longValue());
            facilityDtoList.add(temp);
        }

        facilityServiceImpl.save(facilityDtoList, id);
        System.out.println(facilityList);

        System.out.println("HotelController.write");

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", hotelDto);
        resultMap.put("resultId", id);


        CityEntity cityEntity = cityRepository.findById(hotelDto.getCityId()).get();
        UserEntity userEntity = userRepository.findById(hotelDto.getUserId()).get();
        HotelEntity hotelEntity = HotelEntity.toHotelEntity(hotelDto, cityEntity, userEntity);
        HotelEntity hotel = hotelRepository.save(hotelEntity);
        return hotel.getId();
    }


    @Transactional
    public ResponseEntity<Void> delete(Long id) {
        hotelRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }



    public List<HotelEntity> findAll() {

        List<HotelEntity> hotelEntityList = hotelRepository.findAll();
        List <HotelDto> hotelDtoList = new ArrayList<>();
        for (HotelEntity hotelEntity : hotelEntityList){
            hotelDtoList.add(HotelDto.toAllHotelDto(hotelEntity));
        }
        return hotelEntityList;
    }

    public HotelEntity findById(long id){
        Optional<HotelEntity> optionalHotelEntity = hotelRepository.findById(id);
        if(optionalHotelEntity.isPresent()){
            HotelEntity hotelEntity = optionalHotelEntity.get();
            HashMap<String, Object> hashMap = new HashMap<>();
            List<FileEntity> hotelFileDtoList = fileService.findByHotelId(id);
            List<Long> facilityIdList = hotelService.facilityAll(id);
            List<Long> FacilityIdList = facilityIdList.stream().distinct().collect(Collectors.toList());
            System.out.println(facilityIdList);
            hashMap.put("hotelEntity", hotelService.findById(id));
            hashMap.put("facilities", FacilityIdList);
            hashMap.put("hotelFileDtoList", hotelFileDtoList);

            return hotelEntity;
        } else {
            return null;
        }
    }

    public HotelEntity update(HotelDto hotelDto) {
        CityEntity cityEntity = cityRepository.findById(hotelDto.getCityId()).get();
        UserEntity userEntity = userRepository.findById(hotelDto.getUserId()).get();
        HotelEntity hotelEntity = HotelEntity.updateHotelEntity(hotelDto, cityEntity, userEntity);
        HotelEntity hotel = hotelRepository.save(hotelEntity);

        return findById(hotel.getId());
    }

    public List<Long> facilityAll(long id) {
        List<Long> byHotelEntityId = hotelFacilityRepository.findByHotelEntity_id(id);
        Collections.sort(byHotelEntityId);
        return byHotelEntityId;
    }


}