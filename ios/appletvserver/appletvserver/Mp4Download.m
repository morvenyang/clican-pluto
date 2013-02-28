//
//  Mp4Download.m
//  appletvserver
//
//  Created by zhang wei on 13-2-28.
//
//

#import "Mp4Download.h"

@implementation Mp4Download

@synthesize mp4Url = _mp4Url;
@synthesize mp4DownloadPartials = _mp4DownloadPartials;

- (Mp4DownloadPartial*) getNextDownloadPartial{
    @synchronized(self) {
        if(_downloadIndex<[_mp4DownloadPartials count]){
            Mp4DownloadPartial* next=[_mp4DownloadPartials objectAtIndex:_downloadIndex];
            _downloadIndex++;
            return next;
        }else{
            return nil;
        }
    }
}
- (Mp4DownloadPartial*) getMaxFinishedDownloadPartial{
    Mp4DownloadPartial* partial = NULL;
    int start = _maxFinishedIndex;
    for(int i=start;i<[_mp4DownloadPartials count];i++){
        Mp4DownloadPartial* temp = [_mp4DownloadPartials objectAtIndex:i];
        if(temp.finished){
            partial = temp;
            _maxFinishedIndex = i;
        }else{
            break;
        }
    }
    return partial;
}

- (void) dealloc {
    TT_RELEASE_SAFELY(_mp4DownloadPartials);
    TT_RELEASE_SAFELY(_mp4Url);
    [super dealloc];
}
@end
