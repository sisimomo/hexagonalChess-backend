package com.github.sisimomo.hexagonalchess.backend.configuration.websocket;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import com.github.sisimomo.hexagonalchess.backend.commons.exception.UncheckedException;

public class CustomStompSubProtocolErrorHandler extends StompSubProtocolErrorHandler {

  @Override
  public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
    StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
    if (ex.getCause() instanceof UncheckedException uncheckedException) {
      accessor.setMessage(uncheckedException.getUserMessage());
    } else {
      accessor.setMessage(ex.getMessage());
    }
    accessor.setLeaveMutable(true);

    StompHeaderAccessor clientHeaderAccessor = null;
    if (clientMessage != null) {
      clientHeaderAccessor = MessageHeaderAccessor.getAccessor(clientMessage, StompHeaderAccessor.class);
      if (clientHeaderAccessor != null) {
        String receiptId = clientHeaderAccessor.getReceipt();
        if (receiptId != null) {
          accessor.setReceiptId(receiptId);
        }
      }
    }

    return handleInternal(accessor, new byte[0], ex, clientHeaderAccessor);
  }

}
