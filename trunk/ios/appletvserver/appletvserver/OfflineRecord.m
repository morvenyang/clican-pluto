//
//  OfflineRecord.m
//  appletvserver
//
//  Created by zhang wei on 13-5-30.
//
//

#import "OfflineRecord.h"
#import "AtvUtil.h"
#import "AppDelegate.h"
#import "Constants.h"
@implementation OfflineRecord

@synthesize fileName = _fileName;
@synthesize displayName = _displayName;
@synthesize fileSize = _fileSize;
@synthesize downloadFileSize = _downloadFileSize;
@synthesize filePath = _filePath;
@synthesize fileType = _fileType;
@synthesize url = _url;
@synthesize request = _request;
@synthesize downloading = _downloading;
@synthesize tempSize = _tempSize;

- (void)request:(ASIHTTPRequest *)request didReceiveBytes:(long long)bytes{
    self.tempSize+=bytes;
    if([self.fileType isEqualToString:FILE_TYPE_MP4]){
        self.downloadFileSize+=bytes;
    }
    if(![AtvUtil isWifi]&&!AppDele.ttgNetwork){
        [request clearDelegatesAndCancel];
        self.downloading = FALSE;
    }
}
- (void)request:(ASIHTTPRequest *)request didReceiveResponseHeaders:(NSMutableDictionary *)newResponseHeaders{
    NSString* contentLength = [newResponseHeaders valueForKey:@"Content-Length"];
    NSLog(@"Content-Length:%@",contentLength);
    if(contentLength!=nil&&contentLength.length>0){
        self.fileSize = [contentLength longLongValue];
    }
}
- (void) dealloc {
    TT_RELEASE_SAFELY(_fileName);
    TT_RELEASE_SAFELY(_filePath);
    TT_RELEASE_SAFELY(_fileType);
    TT_RELEASE_SAFELY(_url);
    TT_RELEASE_SAFELY(_displayName);
    TT_RELEASE_SAFELY(_request);
    [super dealloc];
}

-(NSString*) description{
    return [NSString stringWithFormat:@"url:%@,filePath:%@",_url,_filePath];
}
@end
