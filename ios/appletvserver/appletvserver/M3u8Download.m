//
//  M3u8Download.m
//  appletvserver
//
//  Created by zhang wei on 13-2-27.
//
//

#import "M3u8Download.h"

@implementation M3u8Download

@synthesize m3u8DownloadLines = _m3u8DownloadLines;
@synthesize m3u8Url = _m3u8Url;

- (M3u8DownloadLine*) getNextDownloadLine{
     @synchronized(self) {
         if(_downloadIndex<[_m3u8DownloadLines count]){
             M3u8DownloadLine* next=[_m3u8DownloadLines objectAtIndex:_downloadIndex];
             _downloadIndex++;
             return next;
         }else{
             return nil;
         }
     }
}

- (M3u8DownloadLine*) getMaxFinishedDownloadLine{
    M3u8DownloadLine* line = NULL;
    int start = _maxFinishedIndex;
    for(int i=start;i<[_m3u8DownloadLines count];i++){
        M3u8DownloadLine* temp = [_m3u8DownloadLines objectAtIndex:i];
        if(temp.finished){
            line = temp;
            _maxFinishedIndex = i;
        }else{
            break;
        }
    }
    return line;
}
- (void) dealloc {
    TT_RELEASE_SAFELY(_m3u8DownloadLines);
    TT_RELEASE_SAFELY(_m3u8Url);
    [super dealloc];
}

@end
