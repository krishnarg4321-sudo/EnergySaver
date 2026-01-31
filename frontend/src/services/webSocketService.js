import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

class WebSocketService {
  constructor() {
    this.client = null;
    this.connected = false;
    this.subscriptions = new Map();
  }

  connect(userId, onAlertReceived) {
    if (this.connected) {
      console.log('WebSocket already connected');
      return;
    }

    try {
      // Create SockJS instance
      const socket = new SockJS('/ws');

      // Create STOMP client
      this.client = new Client({
        webSocketFactory: () => socket,
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        debug: (str) => {
          console.log('STOMP: ' + str);
        },
        onConnect: () => {
          console.log('WebSocket connected');
          this.connected = true;

          // Subscribe to user-specific alert topic
          const subscription = this.client.subscribe(
            `/topic/alerts/${userId}`,
            (message) => {
              try {
                const alert = JSON.parse(message.body);
                console.log('Received alert:', alert);
                if (onAlertReceived) {
                  onAlertReceived(alert);
                }
              } catch (error) {
                console.error('Error parsing alert message:', error);
              }
            }
          );

          this.subscriptions.set(userId, subscription);
        },
        onStompError: (frame) => {
          console.error('STOMP error:', frame);
          this.connected = false;
        },
        onWebSocketClose: () => {
          console.log('WebSocket closed');
          this.connected = false;
        },
      });

      // Activate the client
      this.client.activate();
    } catch (error) {
      console.error('Error connecting to WebSocket:', error);
    }
  }

  disconnect() {
    if (this.client && this.connected) {
      // Unsubscribe from all topics
      this.subscriptions.forEach((subscription) => {
        subscription.unsubscribe();
      });
      this.subscriptions.clear();

      // Deactivate client
      this.client.deactivate();
      this.connected = false;
      console.log('WebSocket disconnected');
    }
  }

  isConnected() {
    return this.connected;
  }
}

// Export singleton instance
const webSocketService = new WebSocketService();
export default webSocketService;
