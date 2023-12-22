import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// const routes: Routes = [
//     {
//         path: '',
//         component: EntityExtractionComponentComponent,
//         canActivate: [canActivate],
//     },
//     {
//         path: 'language-detection',
//         component: LanguageDetectionComponentComponent,
//         canActivate: [canActivate]
//     },
//     {
//         path: 'sentiment-analysis',
//         component: SentimentAnalysisComponentComponent,
//         canActivate: [canActivate]
//     },
//     {
//         path: 'text-similarity',
//         component: TextSimilarityComponentComponent,
//         canActivate: [canActivate]
//     },
//     {
//         path: 'token-auth',
//         component: TokenAuthComponentComponent
//     },
//     {
//         path: 'history',
//         component: HistoryComponent
//     }
// ];

@NgModule({
    // imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
