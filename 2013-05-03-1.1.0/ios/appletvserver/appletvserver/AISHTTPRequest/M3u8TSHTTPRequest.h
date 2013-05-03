//
//  M3u8TSHTTPRequest.h
//  appletvserver
//
//  Created by zhang wei on 13-2-27.
//
//

#import "ASIHTTPRequest.h"
#import "M3u8Download.h"
#import "M3u8DownloadLine.h"
@interface M3u8TSHTTPRequest : ASIHTTPRequest{
    M3u8Download* _m3u8Download;
    M3u8DownloadLine* _m3u8DownloadLine;
}
@property (nonatomic, retain) M3u8Download* m3u8Download;
@property (nonatomic, retain) M3u8DownloadLine* m3u8DownloadLine;
@end
