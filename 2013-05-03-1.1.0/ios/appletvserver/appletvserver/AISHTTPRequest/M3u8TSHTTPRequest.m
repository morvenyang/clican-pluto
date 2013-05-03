//
//  M3u8TSHTTPRequest.m
//  appletvserver
//
//  Created by zhang wei on 13-2-27.
//
//

#import "M3u8TSHTTPRequest.h"

@implementation M3u8TSHTTPRequest

@synthesize m3u8Download = _m3u8Download;
@synthesize m3u8DownloadLine = _m3u8DownloadLine;
- (void) dealloc {
    TT_RELEASE_SAFELY(_m3u8Download);
    TT_RELEASE_SAFELY(_m3u8DownloadLine);
    [super dealloc];
}

@end
