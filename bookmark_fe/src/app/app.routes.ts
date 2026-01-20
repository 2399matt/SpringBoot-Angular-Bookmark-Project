import {Routes} from '@angular/router';
import {BookMarkComponent} from "./components/book-mark/book-mark.component";
import {AuthGuard} from "@auth0/auth0-angular";
import {BookmarkCreatorComponent} from "./components/bookmark-creator/bookmark-creator.component";
import {BookMarkViewerComponent} from "./components/book-mark-viewer/book-mark-viewer.component";

export const routes: Routes = [
  {path: 'bookmarks', component: BookMarkComponent, canActivate: [AuthGuard]},
  {path: 'create-bookmark', component: BookmarkCreatorComponent, canActivate: [AuthGuard]},
  {path: 'bookmarks/:id', component: BookMarkViewerComponent, canActivate: [AuthGuard]},
  {path: '', component: BookMarkComponent, canActivate: [AuthGuard]},
];
