//
//  Mp4HTTPRequest.m
//  appletvserver
//
//  Created by zhang wei on 13-3-1.
//
//

#import "Mp4HTTPRequest.h"

@implementation Mp4HTTPRequest

@synthesize mp4Download = _mp4Download;
@synthesize mp4DownloadPartial = _mp4DownloadPartial;

- (void) dealloc {
    TT_RELEASE_SAFELY(_mp4Download);
    TT_RELEASE_SAFELY(_mp4DownloadPartial);
    [super dealloc];
}

@end
