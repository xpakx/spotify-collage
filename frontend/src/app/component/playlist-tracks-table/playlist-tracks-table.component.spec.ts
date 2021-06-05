import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlaylistTracksTableComponent } from './playlist-tracks-table.component';

describe('PlaylistTracksTableComponent', () => {
  let component: PlaylistTracksTableComponent;
  let fixture: ComponentFixture<PlaylistTracksTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PlaylistTracksTableComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PlaylistTracksTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
