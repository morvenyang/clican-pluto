//
//  Mp4DownloadPartial.m
//  appletvserver
//
//  Created by zhang wei on 13-2-28.
//
//

#import "Mp4DownloadPartial.h"

@implementation Mp4DownloadPartial

@synthesize startPosition = _startPosition;
@synthesize endPosition = _endPosition;
@synthesize finished = _finished;
@synthesize downloading = _downloading;
@synthesize localPath = _localPath;
- (void) dealloc {
    TT_RELEASE_SAFELY(_localPath);
    [super dealloc];
}

@end
