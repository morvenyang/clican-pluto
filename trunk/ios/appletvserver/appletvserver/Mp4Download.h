//
//  Mp4Download.h
//  appletvserver
//
//  Created by zhang wei on 13-2-28.
//
//

#import "Mp4DownloadPartial.h"

@interface Mp4Download : NSObject{
    NSMutableArray* _mp4DownloadPartials;
    NSString* _mp4Url;
    int _downloadIndex;
    int _maxFinishedIndex;
    long _totalLength;
    long _tempSize;
}
@property (nonatomic, retain) NSMutableArray* mp4DownloadPartials;
@property (nonatomic, copy) NSString* mp4Url;
@property (nonatomic, assign) long totalLength;
@property (atomic, assign) long tempSize;

- (Mp4DownloadPartial*) getNextDownloadPartial;
- (NSData*) getDataByStartPosition:(long) startPosition endPosition:(long) endPosition;
- (void) seekDownloadPartial:(long) position;
-(long) getDurationTempSize;
@end
