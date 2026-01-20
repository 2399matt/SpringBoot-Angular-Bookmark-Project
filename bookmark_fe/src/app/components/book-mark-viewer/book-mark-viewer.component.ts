import { Component } from '@angular/core';
import {BookMark, BookMarkService} from "../../services/book-mark.service";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {DatePipe} from "@angular/common";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";

@Component({
  selector: 'app-book-mark-viewer',
  standalone: true,
  imports: [
    DatePipe,
    RouterLink,
    ReactiveFormsModule
  ],
  templateUrl: './book-mark-viewer.component.html',
  styleUrl: './book-mark-viewer.component.css'
})
export class BookMarkViewerComponent {

  readonly FAVICON_PLACEHOLDER: string = 'assets/bmplaceholder.png';
  bookmark: BookMark | null = null;
  editMode: boolean = false;
  form: FormGroup;

  constructor(private bookMarkService: BookMarkService, private route: ActivatedRoute, private router: Router) {
    this.form = new FormGroup({
      title: new FormControl('', [Validators.required, Validators.maxLength(145)]),
      description: new FormControl('', [Validators.required, Validators.maxLength(500)])
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id')) ?? 1;
      this.fetchBookMark(id);
    });
  }

  fetchBookMark(id: number) {
    this.bookMarkService.getBookMark(id).subscribe({
      next: (res) => {
        this.bookmark = res;
        this.form.patchValue({
          title: this.bookmark.title,
          description: this.bookmark.description
        });
      },
      error: (err) => console.log(err)
    });
  }

  updateViews(bookId: number): void {
    this.bookMarkService.incrementViews(bookId).subscribe({
      next: () => {
        if(this.bookmark) this.bookmark.views++;
      },
      error: (err) => console.log(err)
    });
  }

  toggleEditMode(): void {
    this.editMode = !this.editMode;
  }

  onSubmit(): void {
    if(this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const title: string = this.title?.value;
    const description: string = this.description?.value;
    const id: number = Number(this.bookmark?.id);
    this.bookMarkService.updateBookMark(id, title, description).subscribe({
      next: (res) => {
        this.bookmark = res;
        this.form.patchValue({
          title: this.bookmark.title,
          description: this.bookmark.description
        });
        this.form.markAsPristine();
        this.editMode = false;
      },
      error: (err) => console.log(err)
    });
  }

  deleteBookMark(): void {
    if(confirm("Delete this bookmark?")) {
      if(this.bookmark) {
        this.bookMarkService.deleteBookMark(this.bookmark.id).subscribe({
          next: () => {
            this.router.navigate(['/bookmarks'], {});
          },
          error: (err) => {
            console.log(err);
            this.router.navigate(['/bookmarks'], {});
          }
        });
      }
    }
  }

  onFaviconLoad(event: Event): void {
    const target = event.target as HTMLImageElement;
    // can't catch the 404, Google's default image is 16 while we request 64. Sufficient.
    if(target.naturalWidth === 16 && target.naturalHeight === 16) {
      target.src = this.FAVICON_PLACEHOLDER;
    }
  }

  get title() {
    return this.form.get('title');
  }

  get description() {
    return this.form.get('description');
  }

}
