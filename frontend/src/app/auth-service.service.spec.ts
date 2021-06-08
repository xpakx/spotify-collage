import { TestBed } from '@angular/core/testing';

import { SpotifyService } from './auth-service.service';

describe('AuthServiceService', () => {
  let service: SpotifyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SpotifyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
