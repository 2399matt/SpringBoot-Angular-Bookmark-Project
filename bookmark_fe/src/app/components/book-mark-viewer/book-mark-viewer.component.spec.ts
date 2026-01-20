import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookMarkViewerComponent } from './book-mark-viewer.component';

describe('BookMarkViewerComponent', () => {
  let component: BookMarkViewerComponent;
  let fixture: ComponentFixture<BookMarkViewerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookMarkViewerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BookMarkViewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
