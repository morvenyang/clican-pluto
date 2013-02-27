//
//  M3u8Process.h
//  appletvserver
//
//  Created by zhang wei on 13-2-27.
//
//

#import <Foundation/Foundation.h>
#import "M3u8Download.h"

@interface M3u8Process : NSObject{
    
}

-(NSString*) doSyncRequestByM3U8Url:(NSString*) url;

-(void) addAsyncM3u8TSRequestByM3u8Download:(M3u8Download*) m3u8Download;

@end
