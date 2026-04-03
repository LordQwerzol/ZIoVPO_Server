package ZIoVPO.ZIoVPO_Server.util;

import ZIoVPO.ZIoVPO_Server.entity.MalwareSignature;
import ZIoVPO.ZIoVPO_Server.entity.MalwareSignatureAudit;
import ZIoVPO.ZIoVPO_Server.entity.MalwareSignatureHistory;
import ZIoVPO.ZIoVPO_Server.model.*;
import ZIoVPO.ZIoVPO_Server.model.enums.SignatureStatus;
import org.springframework.stereotype.Component;

@Component
public class MappingUtils {

    public MalwareSignature toEntity(MalwareSignatureRequest request) {
        MalwareSignature entity = new MalwareSignature();
        entity.setThreatName(request.getThreatName());
        entity.setFirstBytesHex(request.getFirstBytesHex());
        entity.setRemainderHashHex(request.getRemainderHashHex());
        entity.setRemainderLength(request.getRemainderLength());
        entity.setFileType(request.getFileType());
        entity.setOffsetStart(request.getOffsetStart());
        entity.setOffsetEnd(request.getOffsetEnd());
        return entity;
    }

    public MalwareSignatureResponse toResponse (MalwareSignature entity) {
        MalwareSignatureResponse response = new MalwareSignatureResponse();
        response.setId(entity.getId());
        response.setThreatName(entity.getThreatName());
        response.setFirstBytesHex(entity.getFirstBytesHex());
        response.setRemainderHashHex(entity.getRemainderHashHex());
        response.setRemainderLength(entity.getRemainderLength());
        response.setFileType(entity.getFileType());
        response.setOffsetStart(entity.getOffsetStart());
        response.setOffsetEnd(entity.getOffsetEnd());
        response.setUpdatedAt(entity.getUpdatedAt().toString());
        response.setStatus(entity.getStatus().toString());
        response.setDigitalSignatureBase64(entity.getDigitalSignatureBase64());
        return response;
    }

    public SignedMalwareData toSignedMalwareData (MalwareSignatureRequest request, SignatureStatus status) {
        SignedMalwareData payload = new SignedMalwareData();
        payload.setThreatName(request.getThreatName());
        payload.setFirstBytesHex(request.getFirstBytesHex());
        payload.setRemainderHashHex(request.getRemainderHashHex());
        payload.setRemainderLength(request.getRemainderLength());
        payload.setFileType(request.getFileType());
        payload.setOffsetStart(request.getOffsetStart());
        payload.setOffsetEnd(request.getOffsetEnd());
        payload.setStatus(status.toString());
        return payload;
    }

    public SignedMalwareData toSignedMalwareData (MalwareSignature entity) {
        SignedMalwareData payload = new SignedMalwareData();
        payload.setThreatName(entity.getThreatName());
        payload.setFirstBytesHex(entity.getFirstBytesHex());
        payload.setRemainderHashHex(entity.getRemainderHashHex());
        payload.setRemainderLength(entity.getRemainderLength());
        payload.setFileType(entity.getFileType());
        payload.setOffsetStart(entity.getOffsetStart());
        payload.setOffsetEnd(entity.getOffsetEnd());
        payload.setStatus(entity.getStatus().toString());
        return payload;
    }


    public MalwareSignatureHistoryResponse toResponse (MalwareSignatureHistory entity){
        MalwareSignatureHistoryResponse response = new MalwareSignatureHistoryResponse();
        response.setId(entity.getSignatureId());
        response.setThreatName(entity.getThreatName());
        response.setFirstBytesHex(entity.getFirstBytesHex());
        response.setRemainderHashHex(entity.getRemainderHashHex());
        response.setRemainderLength(entity.getRemainderLength());
        response.setFileType(entity.getFileType());
        response.setOffsetStart(entity.getOffsetStart());
        response.setOffsetEnd(entity.getOffsetEnd());
        response.setUpdatedAt(entity.getUpdatedAt().toString());
        response.setStatus(entity.getStatus().toString());
        response.setVersionCreatedAt(entity.getVersionCreatedAt().toString());
        response.setDigitalSignatureBase64(entity.getDigitalSignatureBase64());
        return response;
    }

    public MalwareSignatureAuditResponse toResponse (MalwareSignatureAudit entity){
        MalwareSignatureAuditResponse response = new MalwareSignatureAuditResponse();
        response.setUserEmail(entity.getChangedBy().getEmail());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setFieldsChanged(entity.getFieldsChanged());
        response.setDescription(entity.getDescription());
        return response;
    }

    public MalwareSignatureHistory toSaveOldSignature (MalwareSignature old){
        MalwareSignatureHistory record = new MalwareSignatureHistory();
        record.setSignatureId(old.getId());
        record.setThreatName(old.getThreatName());
        record.setFirstBytesHex(old.getFirstBytesHex());
        record.setRemainderHashHex(old.getRemainderHashHex());
        record.setRemainderLength(old.getRemainderLength());
        record.setFileType(old.getFileType());
        record.setOffsetStart(old.getOffsetStart());
        record.setOffsetEnd(old.getOffsetEnd());
        record.setUpdatedAt(old.getUpdatedAt());
        record.setStatus(old.getStatus());
        record.setDigitalSignatureBase64(old.getDigitalSignatureBase64());
        return record;
    }
}
