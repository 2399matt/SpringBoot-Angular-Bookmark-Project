import { Component } from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {BookMarkService} from "../../services/book-mark.service";
import {UrlValidator} from "../../services/url-validator";
import {Router} from "@angular/router";
import {TagResponse, TagService} from "../../services/tag.service";

@Component({
  selector: 'app-bookmark-creator',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './bookmark-creator.component.html',
  styleUrl: './bookmark-creator.component.css'
})
export class BookmarkCreatorComponent {

  bookmarkForm: FormGroup;
  tags: TagResponse[] = [];
  urlIssue: boolean = false;

  constructor(private bookmarkService: BookMarkService, private router: Router, private tagService: TagService) {
    this.bookmarkForm = new FormGroup({
      url: new FormControl('', [Validators.required, UrlValidator.isValidUrl]),
      tagIds: new FormControl<number[]>([], [])
    });
  }

  ngOnInit(): void {
    this.tagService.fetchTags().subscribe({
      next: (res) => this.tags = res,
      error: err => console.log(err)
    });
    this.bookmarkForm.get('url')?.valueChanges.subscribe(() => {
      this.urlIssue = false;
    });
  }

  onSubmit(): void {
    if(this.bookmarkForm.invalid){
      this.bookmarkForm.markAllAsTouched();
      return;
    }
    const url: string = this.url?.value;
    const tagIds: number[] = this.tagIds?.value;
    this.bookmarkService.createBookMark(url, tagIds).subscribe({
      next: () => this.router.navigate(['/bookmarks']),
      error: err => {
        console.log(err);
        this.urlIssue = true;
      }
    });
  }

  isTagSelected(tagId: number): boolean {
    const ids: number[] = this.bookmarkForm.get('tagIds')?.value;
    return ids.includes(tagId);
  }

  onTagToggle(tagId: number, event: Event): void {
    const checked: boolean = (event.target as HTMLInputElement).checked;
    const currentTags: number[] = this.bookmarkForm.get('tagIds')?.value;
    if(checked) {
      this.bookmarkForm.patchValue({
        tagIds: currentTags.concat(tagId)
      });
    } else {
      this.bookmarkForm.patchValue({
        tagIds: currentTags.filter(id => id !== tagId)
      });
    }
  }

  get url() {
    return this.bookmarkForm.get('url');
  }

  get tagIds() {
    return this.bookmarkForm.get('tagIds');
  }
}
