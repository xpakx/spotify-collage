import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SpotifyAddress } from './model/spotify-address';
import { Observable } from 'rxjs';
import { Collage } from './model/collage';
import { CollageRequest } from './model/collage-request';
import { TokenResponse } from './model/token-response';
import { TokenRequest } from './model/token-request';

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {
  url = "http://192.168.1.204:8080"

  constructor(private http: HttpClient) { }

  public getAddress(): Observable<SpotifyAddress> {
    return this.http.get<SpotifyAddress>(`${this.url}/login`);
  }

  public getCollage(request: CollageRequest): Observable<Collage> {
    return this.http.post<Collage>(`${this.url}/image`, request);
  }

  public getToken(request: TokenRequest): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(`${this.url}/token`, request);
  }
}
