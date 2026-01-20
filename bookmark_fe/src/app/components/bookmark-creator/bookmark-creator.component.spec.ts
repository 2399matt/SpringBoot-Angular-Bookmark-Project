import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookmarkCreatorComponent } from './bookmark-creator.component';

describe('BookmarkCreatorComponent', () => {
  let component: BookmarkCreatorComponent;
  let fixture: ComponentFixture<BookmarkCreatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookmarkCreatorComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BookmarkCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
