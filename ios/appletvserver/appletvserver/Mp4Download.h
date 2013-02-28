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
}
@property (nonatomic, retain) NSMutableArray* mp4DownloadPartials;
@property (nonatomic, copy) NSString* mp4Url;

- (Mp4DownloadPartial*) getNextDownloadPartial;
- (Mp4DownloadPartial*) getMaxFinishedDownloadPartial;

@end
