//
//  Mp4HTTPRequest.h
//  appletvserver
//
//  Created by zhang wei on 13-3-1.
//
//

#import "ASIHTTPRequest.h"
#import "Mp4Download.h"
#import "Mp4DownloadPartial.h"

@interface Mp4HTTPRequest : ASIHTTPRequest{
    Mp4Download* _mp4Download;
    Mp4DownloadPartial* _mp4DownloadPartial;
}

@property (nonatomic, retain) Mp4Download* mp4Download;
@property (nonatomic, retain) Mp4DownloadPartial* mp4DownloadPartial;

@end
