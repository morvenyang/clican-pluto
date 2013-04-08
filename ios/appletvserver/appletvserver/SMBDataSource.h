//
//  SMBDataSource.h
//  appletvserver
//
//  Created by zhang wei on 13-4-8.
//
//

#import <Three20UI/Three20UI.h>

@interface SMBDataSource : TTListDataSource{
    NSString* _url;
}
@property (nonatomic, copy) NSString    *url;

-(id) initWithUrl:(NSString*) url;
@end
