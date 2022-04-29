import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NAVComponent } from './nav.component';

describe('NAVComponent', () => {
  let component: NAVComponent;
  let fixture: ComponentFixture<NAVComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NAVComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NAVComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
