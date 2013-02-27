//
//  M3u8TSHTTPRequest.h
//  appletvserver
//
//  Created by zhang wei on 13-2-27.
//
//

#import "ASIHTTPRequest.h"
#import "M3u8Download.h"
@interface M3u8TSHTTPRequest : ASIHTTPRequest{
    M3u8Download* _m3u8Download;
}
@property (nonatomic, retain) M3u8Download* m3u8Download;

@end
