import { Offer } from './offer.model';
import { Message } from './message.model';

export interface Pool {
  id?: number;
  name?: string;
  photo?: string;
  direction?: string;
  capacity?: number;
  scheduleStart?: string; // Consider changing to Date type if needed
  scheduleEnd?: string; // Consider changing to Date type if needed
  company?: string;
  description?: string;
  messages?: Message[];
  image?: string;
  offers?: Offer[];
}