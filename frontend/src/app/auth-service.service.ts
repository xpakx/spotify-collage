import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { SpotifyAddress } from './model/spotify-address';
import { Observable } from 'rxjs';
import { Collage } from './model/collage';
import { CollageRequest } from './model/collage-request';
import { TokenResponse } from './model/token-response';
import { TokenRequest } from './model/token-request';
import { Playlist } from './model/playlist';
import { Page } from './model/page';
import { Token } from './model/token';
import { Track } from './model/track';
import { TrackWrapper } from './model/track-wrapper';

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
	let params = new HttpParams();
    params = params.append('token', request.token);
    return this.http.get<Collage>(`${this.url}/image`, { params: params });
  }

  public getToken(request: TokenRequest): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(`${this.url}/token`, request);
  }

  public getPlaylists(request: Token): Observable<Page<Playlist>> {
	let params = new HttpParams();
    params = params.append('token', request.token);
    return this.http.get<Page<Playlist>>(`${this.url}/playlists`, { params: params });
  }

  public getPlaylistTracks(request: Token, id: String): Observable<Page<TrackWrapper>> {
	let params = new HttpParams();
    params = params.append('token', request.token);
    return this.http.get<Page<TrackWrapper>>(`${this.url}/playlists/${id}`, { params: params });
  }

  public getPlaylistCollage(token: string, id: String): Observable<Blob> {
    let params = new HttpParams();
    params = params.append('token', token);
    return this.http.get(`${this.url}/playlists/${id}/image`, { responseType: 'blob', params: params });
  }
}
