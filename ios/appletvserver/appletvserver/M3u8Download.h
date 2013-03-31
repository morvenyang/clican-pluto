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
    NSString* _m3u8Url;
    int _downloadIndex;
    int _maxFinishedIndex;
}

@property (nonatomic, retain) NSMutableArray* m3u8DownloadLines;
@property (nonatomic, copy) NSString* m3u8Url;
- (M3u8DownloadLine*) getNextDownloadLine;
- (M3u8DownloadLine*) getMaxFinishedDownloadLine;
-(void) seekDownloadLine:(NSString*) localPath;

@end
