//
//  URLDataHeaderResponse.h
//  appletvserver
//
//  Created by zhang wei on 13-3-26.
//
//

#import <Three20Network/Three20Network.h>

@interface URLDataHeaderResponse : TTURLDataResponse{
    NSDictionary* _allHeaders;
}
@property (nonatomic, retain) NSDictionary* allHeaders;

@end
