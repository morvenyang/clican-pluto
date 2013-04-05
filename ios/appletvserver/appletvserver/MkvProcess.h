//
//  MkvProcess.h
//  appletvserver
//
//  Created by zhang wei on 13-4-5.
//
//

#import <Foundation/Foundation.h>

@interface MkvProcess : NSObject{
    NSString* _mkvUrl;
    NSString* _m3u8Url;
    BOOL _running;
}

@property (nonatomic, copy) NSString* mkvUrl;
@property (nonatomic, copy) NSString* m3u8Url;
@property (nonatomic, assign) BOOL running;

-(NSString*) convertToM3u8MkvUrl:(NSString*) url;

@end
