import { Person } from './person.model';
import { Offer } from './offer.model';

export interface Employer extends Person {
  photo?: Blob;
  imageCompany?: boolean;
  company?: string;
  offers?: Offer[];
}