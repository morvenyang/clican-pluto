//
//  OfflineRecord.m
//  appletvserver
//
//  Created by zhang wei on 13-5-30.
//
//

#import "OfflineRecord.h"

@implementation OfflineRecord

@synthesize fileName = _fileName;
@synthesize displayName = _displayName;
@synthesize fileSize = _fileSize;
@synthesize downloadFileSize = _downloadFileSize;
@synthesize filePath = _filePath;
@synthesize fileType = _fileType;
@synthesize url = _url;
@synthesize request = _request;

- (void)request:(ASIHTTPRequest *)request didReceiveBytes:(long long)bytes{
    self.downloadFileSize+=bytes;
}

- (void) dealloc {
    TT_RELEASE_SAFELY(_fileName);
    TT_RELEASE_SAFELY(_filePath);
    TT_RELEASE_SAFELY(_fileType);
    TT_RELEASE_SAFELY(_url);
    [super dealloc];
}
@end
