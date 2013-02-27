//
//  M3u8Download.h
//  appletvserver
//
//  Created by zhang wei on 13-2-27.
//
//
#import "M3u8DownloadLine.h"

@interface M3u8Download : NSObject{
    NSMutableArray* _m3u8DownloadLines;
    int _downloadIndex;
}

@property (nonatomic, retain) NSMutableArray* m3u8DownloadLines;

- (M3u8DownloadLine*) getNextDownloadLine;
@end
