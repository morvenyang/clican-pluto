//
//  M3u8DownloadLine.m
//  appletvserver
//
//  Created by zhang wei on 13-2-27.
//
//

#import "M3u8DownloadLine.h"

@implementation M3u8DownloadLine

@synthesize originalUrl = _originalUrl;
@synthesize localUrl = _localUrl;
@synthesize localPath = _localPath;
@synthesize finished = _finished;

- (void) dealloc {
    TT_RELEASE_SAFELY(_originalUrl);
    TT_RELEASE_SAFELY(_localUrl);
    TT_RELEASE_SAFELY(_localPath);
    [super dealloc];
}

@end
