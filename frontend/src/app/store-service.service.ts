import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StoreService {
  public token: string | null = null;
  public username: string | null = null;
}
