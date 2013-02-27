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

- (void) dealloc {
    TT_RELEASE_SAFELY(_m3u8DownloadLines);
    [super dealloc];
}

@end
