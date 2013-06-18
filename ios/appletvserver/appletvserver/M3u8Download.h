//
//  M3u8Download.h
//  appletvserver
//
//  Created by zhang wei on 13-2-27.
//
//
#import "M3u8DownloadLine.h"
#import "ASIHTTPRequest.h"

@interface M3u8Download : NSObject<ASIProgressDelegate>{
    NSMutableArray* _m3u8DownloadLines;
    NSString* _m3u8Url;
    int _downloadIndex;
    long _tempSize;
}

@property (nonatomic, retain) NSMutableArray* m3u8DownloadLines;
@property (nonatomic, copy) NSString* m3u8Url;
@property (atomic, assign) long tempSize;
- (M3u8DownloadLine*) getNextDownloadLine;
-(void) seekDownloadLine:(NSString*) localPath;
-(long) getDurationTempSize;
@end
