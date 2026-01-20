import {Component} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {BookMark, BookMarkService} from "../../services/book-mark.service";
import {DatePipe} from "@angular/common";
import {FormControl, ReactiveFormsModule} from "@angular/forms";
import {debounceTime, distinctUntilChanged} from "rxjs";

@Component({
  selector: 'app-book-mark',
  standalone: true,
  imports: [
    DatePipe,
    RouterLink,
    ReactiveFormsModule
  ],
  templateUrl: './book-mark.component.html',
  styleUrl: './book-mark.component.css'
})
export class BookMarkComponent {

  readonly FAVICON_PLACEHOLDER: string = 'assets/bmplaceholder.png';
  currentPage: number = 1;
  totalPages: number = 1;
  pageSize: number = 15;
  bookmarks: BookMark[] = [];
  activeTags: number[] = [];
  searchTerm: FormControl = new FormControl();


  constructor(private route: ActivatedRoute, private router: Router, private bookmarkService: BookMarkService) {

  }

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(param => {
      const tags = param.get('tags');
      const page = param.get('page');
      const searchTerm = param.get('q') ?? '';
      const newPage = page ? Number(page) : 1;
      const newTags = tags ? tags.split(',').map(Number) : [];
      const changedTags: boolean = JSON.stringify(this.activeTags) !== JSON.stringify(newTags);
      this.activeTags = newTags;
      if (changedTags) {
        this.currentPage = 1;
      } else {
        this.currentPage = newPage;
      }
      this.fetchBookmarks(searchTerm);
    });
    this.searchTerm.valueChanges.pipe(
      debounceTime(500),
      distinctUntilChanged()
    ).subscribe(value => {
      this.router.navigate(['/bookmarks'], {
        queryParams: {
          page: 1,
          q: value,
          tags: this.activeTags.length > 0 ? this.activeTags.join(',') : null
        }
      });
    });
  }

  fetchBookmarks(searchTerm: string): void {
    this.bookmarkService.getBookMarks(searchTerm, this.activeTags, this.currentPage, this.pageSize).subscribe({
      next: res => {
        this.bookmarks = res.content;
        this.totalPages = res.page.totalPages;
        this.pageSize = res.page.size;
      },
      error: err => console.log(err)
    });
  }

  goToPage(page: number): void {
    this.router.navigate(['/bookmarks'], {
      queryParams: {
        tags: this.activeTags.length > 0 ? this.activeTags.join(',') : null,
        page: page,
        q: this.searchTerm.value ? this.searchTerm.value : null
      }, queryParamsHandling: 'merge'
    });
  }

  updateViews(bookId: number): void {
    this.bookmarkService.incrementViews(bookId).subscribe({
      next: () => {
        const bookmark = this.bookmarks.find((bm) => bm.id === bookId);
        if (bookmark) {
          bookmark.views++;
        }
      },
      error: err => console.log(err)
    });
  }

  onFaviconLoad(event: Event): void {
    const target = event.target as HTMLImageElement;
    // can't catch the 404 for some reason, Google's default image is 16 while we request 64.
    if(target.naturalWidth === 16 && target.naturalHeight === 16) {
      target.src = this.FAVICON_PLACEHOLDER;
    }
  }
}
